package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateUserRequest;
import co.edu.usbcali.ecommerceusb.dto.UpdateUserRequest;
import co.edu.usbcali.ecommerceusb.dto.UserResponse;
import co.edu.usbcali.ecommerceusb.mapper.UserMapper;
import co.edu.usbcali.ecommerceusb.model.DocumentType;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.DocumentTypeRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) return List.of();
        return UserMapper.modelToUserResponseList(users);
    }

    @Override
    public UserResponse getUserById(Integer id) throws Exception {
        if (id == null) throw new Exception("Debe ingresar el id para buscar");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(String.format("Usuario no encontrado con el id: %d", id)));
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) throws Exception {
        if (email == null || email.isBlank()) throw new Exception("Debe ingresar email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception(String.format("Usuario no encontrado con el email: %s", email)));
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) throws Exception {

        if (Objects.isNull(createUserRequest.getFullName()) || createUserRequest.getFullName().isBlank())
            throw new Exception("El campo fullName no puede ser nulo ni vacío");
        if (Objects.isNull(createUserRequest.getPhone()) || createUserRequest.getPhone().isBlank())
            throw new Exception("El campo phone no puede ser nulo ni vacío");
        if (Objects.isNull(createUserRequest.getEmail()) || createUserRequest.getEmail().isBlank())
            throw new Exception("El campo email no puede ser nulo ni vacío");
        if (createUserRequest.getDocumentTypeId() == null || createUserRequest.getDocumentTypeId() <= 0)
            throw new Exception("El campo documentTypeId debe contener un valor mayor a 0");
        if (Objects.isNull(createUserRequest.getDocumentNumber()) || createUserRequest.getDocumentNumber().isBlank())
            throw new Exception("El campo documentNumber no puede estar nulo ni vacío");
        if (Objects.isNull(createUserRequest.getBirthDate()) || createUserRequest.getBirthDate().isBlank())
            throw new Exception("El campo birthDate no puede estar nulo ni vacío");
        if (Objects.isNull(createUserRequest.getCountry()) || createUserRequest.getCountry().isBlank())
            throw new Exception("El campo country no puede estar nulo ni vacío");
        if (Objects.isNull(createUserRequest.getAddress()) || createUserRequest.getAddress().isBlank())
            throw new Exception("El campo address no puede estar nulo ni vacío");

        DocumentType documentType = documentTypeRepository.findById(createUserRequest.getDocumentTypeId())
                .orElseThrow(() -> new Exception("El tipo de documento no existe"));

        if (userRepository.existsByEmail(createUserRequest.getEmail()))
            throw new Exception("Ya existe un usuario con el email ingresado");
        if (userRepository.existsByDocumentNumberAndDocumentTypeId(
                createUserRequest.getDocumentNumber(), createUserRequest.getDocumentTypeId()))
            throw new Exception("Ya existe un usuario con el documento ingresado");

        User user = UserMapper.createUserRequestToUser(createUserRequest, documentType);
        user = userRepository.save(user);
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Integer id, UpdateUserRequest updateUserRequest) throws Exception {

        if (id == null || id <= 0) throw new Exception("Debe ingresar el id para actualizar");
        if (Objects.isNull(updateUserRequest.getFullName()) || updateUserRequest.getFullName().isBlank())
            throw new Exception("El campo fullName no puede ser nulo ni vacío");
        if (Objects.isNull(updateUserRequest.getPhone()) || updateUserRequest.getPhone().isBlank())
            throw new Exception("El campo phone no puede ser nulo ni vacío");
        if (Objects.isNull(updateUserRequest.getEmail()) || updateUserRequest.getEmail().isBlank())
            throw new Exception("El campo email no puede ser nulo ni vacío");
        if (updateUserRequest.getDocumentTypeId() == null || updateUserRequest.getDocumentTypeId() <= 0)
            throw new Exception("El campo documentTypeId debe contener un valor mayor a 0");
        if (Objects.isNull(updateUserRequest.getDocumentNumber()) || updateUserRequest.getDocumentNumber().isBlank())
            throw new Exception("El campo documentNumber no puede estar nulo ni vacío");
        if (Objects.isNull(updateUserRequest.getBirthDate()) || updateUserRequest.getBirthDate().isBlank())
            throw new Exception("El campo birthDate no puede estar nulo ni vacío");
        if (Objects.isNull(updateUserRequest.getCountry()) || updateUserRequest.getCountry().isBlank())
            throw new Exception("El campo country no puede estar nulo ni vacío");
        if (Objects.isNull(updateUserRequest.getAddress()) || updateUserRequest.getAddress().isBlank())
            throw new Exception("El campo address no puede estar nulo ni vacío");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(String.format("Usuario no encontrado con el id: %d", id)));

        DocumentType documentType = documentTypeRepository.findById(updateUserRequest.getDocumentTypeId())
                .orElseThrow(() -> new Exception("El tipo de documento no existe"));

        // Validar email único solo si cambió
        if (!user.getEmail().equals(updateUserRequest.getEmail()) &&
                userRepository.existsByEmail(updateUserRequest.getEmail()))
            throw new Exception("Ya existe un usuario con el email ingresado");

        // Validar documento único solo si cambió
        if ((!user.getDocumentNumber().equals(updateUserRequest.getDocumentNumber()) ||
                !user.getDocumentType().getId().equals(updateUserRequest.getDocumentTypeId())) &&
                userRepository.existsByDocumentNumberAndDocumentTypeId(
                        updateUserRequest.getDocumentNumber(), updateUserRequest.getDocumentTypeId()))
            throw new Exception("Ya existe un usuario con el documento ingresado");

        user.setFullName(updateUserRequest.getFullName());
        user.setPhone(updateUserRequest.getPhone());
        user.setEmail(updateUserRequest.getEmail());
        user.setDocumentType(documentType);
        user.setDocumentNumber(updateUserRequest.getDocumentNumber());
        user.setBirthDate(LocalDate.parse(updateUserRequest.getBirthDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        user.setCountry(updateUserRequest.getCountry());
        user.setAddress(updateUserRequest.getAddress());
        user.setUpdatedAt(OffsetDateTime.now());

        user = userRepository.save(user);
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public void deleteUser(Integer id) throws Exception {
        if (id == null || id <= 0) throw new Exception("Debe ingresar el id para eliminar");
        if (!userRepository.existsById(id))
            throw new Exception(String.format("Usuario no encontrado con el id: %d", id));
        userRepository.deleteById(id);
    }
}
