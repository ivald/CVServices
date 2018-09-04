package edu.ilyav.api.service;

import edu.ilyav.api.models.Education;
import edu.ilyav.api.service.exceptions.ResourceNotFoundException;

import java.util.List;

public interface EducationService {
	List<Education> findAll();

	Education findById(Long id);

	String delete(Long id) throws Exception;

	Education saveOrUpdate(Education experience) throws ResourceNotFoundException;

}
