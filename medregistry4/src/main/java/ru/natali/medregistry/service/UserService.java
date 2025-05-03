package ru.natali.medregistry.service;

import ru.natali.medregistry.dto.UserDTO;
import ru.natali.medregistry.model.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO convertToDto(User user);
    User convertToEntity(UserDTO userDTO);
}