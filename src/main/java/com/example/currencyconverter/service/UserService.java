package com.example.currencyconverter.service;

import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.dto.UserDto;
import com.example.currencyconverter.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    public boolean registerUser(UserDto userDto) {
        try {
            loadUserByUsername(userDto.getUsername());
            return false;
        } catch (UsernameNotFoundException ignored) {
        }
        User user = new User(userDto.getUsername(), encoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return true;
    }
}
