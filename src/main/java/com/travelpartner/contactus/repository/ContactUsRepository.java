package com.travelpartner.contactus.repository;

import com.travelpartner.contactus.model.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
}