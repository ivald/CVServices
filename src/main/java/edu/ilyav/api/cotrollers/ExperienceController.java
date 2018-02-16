package edu.ilyav.api.cotrollers;

import edu.ilyav.api.models.Experience;
import edu.ilyav.api.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/private/experience")
public class ExperienceController {

	@Autowired
	private ExperienceService experienceService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Experience add(@RequestBody Experience experience) {
		return experienceService.saveOrUpdate(experience);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Experience update(@RequestBody Experience experience) {
		return experienceService.saveOrUpdate(experience);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Experience> getAllUsers() {
		return experienceService.findAllProfiles();
	}


}