package edu.ilyav.api.service;

import edu.ilyav.api.models.Experience;
import edu.ilyav.api.models.WebResponse;
import edu.ilyav.api.service.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ExperienceService {
	List<Experience> findAll();

	Experience findById(Long id);

	WebResponse delete(Long id) throws Exception;

	Experience saveOrUpdate(Experience experience) throws ResourceNotFoundException;

}
