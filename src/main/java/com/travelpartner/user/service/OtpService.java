package com.travelpartner.user.service;

import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;

    // CREATE OR GENERATE OTP
    public String createOrGenerateOtp(String mobileNumber) {

        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElse(null);

        // ======================================================
        // 🔴 TEMPORARY OTP LOGIC (STATIC OTP FOR TESTING ONLY)
        // ======================================================
        String otp = "1234";

        // ======================================================
        // ORIGINAL OTP LOGIC (COMMENTED FOR NOW)
        // ======================================================
        /*
        String otp = generateRandomOtp();
        */

        // CASE 1: USER EXISTS → update OTP
        if (user != null) {
            user.setOtp(otp);
            userRepository.save(user);
        }

        // CASE 2: USER NOT EXISTS → create new user with OTP
        else {
            user = User.builder()
                    .mobileNumber(mobileNumber)
                    .otp(otp)
                    .platform(Platform.ADMIN_PORTAL)
                    .isOtpVerified(false)
                    .build();

            userRepository.save(user);
        }

        return otp;
    }

    // Validate OTP (keep existing logic)
    public boolean validateOtp(String mobileNumber, String otp) {
        return userRepository.findByMobileNumber(mobileNumber)
                .map(user -> otp.equals(user.getOtp()))
                .orElse(false);
    }

    // ======================================================
    // ORIGINAL RANDOM OTP GENERATOR (COMMENTED FOR NOW)
    // ======================================================
    /*
    private String generateRandomOtp() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }
    */
}