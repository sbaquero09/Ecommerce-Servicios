package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.UserResponse;
import co.edu.usbcali.ecommerceusb.model.DocumentType;
import co.edu.usbcali.ecommerceusb.model.User;

import java.util.List;

public class UserMapper {
    // User (Entity) -> UserResponse (DTO)
    public static UserResponse modelToUserResponse(User user){

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                //Uso de if ternario
                .documentTypeId(
                        user.getDocumentType() != null ? user.getDocumentType().getId() : null)
                //uso de if ternario
                .documentTypeName(
                        user.getDocumentType() != null ? user.getDocumentType().getName() : null)
                .documentNumber(user.getDocumentNumber())
                .build();
    }

    public static List<UserResponse> modelToUserResponseList(
            List<User> users){
        return users.stream().map(UserMapper::modelToUserResponse).toList();
    }
}


