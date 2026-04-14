package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.UserResponse;
import co.edu.usbcali.ecommerceusb.mapper.UserMapper;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    //Inyeccion de indenpendencias de UserRepository
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponse> getUsers() {
        //Definir una lista de Users
        List<User> users = userRepository.findAll();

        //Validar si la lista esta vacia
        if (users.isEmpty()){
            return List.of();
        }

        List<UserResponse> userResponses = UserMapper.modelToUserResponseList(users);
        return userResponses;
    }

    @Override
    public UserResponse getUserById(Integer id) throws Exception {
        //Validar que venga un valor en id
        if (id == null){
            throw new Exception("Debe ingresar el id para buscar");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                String.format("Usuario no encontrado con el id: %d", id)));
        UserResponse userResponse = UserMapper.modelToUserResponse(user);
        return userResponse;
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return null;
    }
}
