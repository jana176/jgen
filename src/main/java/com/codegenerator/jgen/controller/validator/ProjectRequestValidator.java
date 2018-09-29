package com.codegenerator.jgen.controller.validator;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;

import com.codegenerator.jgen.handler.model.ClassData;
import com.codegenerator.jgen.handler.model.Controller;
import com.codegenerator.jgen.handler.model.ControllerOperations;
import com.codegenerator.jgen.handler.model.Project;
import com.codegenerator.jgen.handler.model.ServiceOperations;

@Service
public class ProjectRequestValidator {

	public void validate(Project project) {
		List<ClassData> classes = project.getClasses();
		classes.forEach(classData -> {
			try {
				validateServiceControllerRelation(classData, classData.getService(), classData.getController());
			} catch (ServletRequestBindingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	private void validateServiceControllerRelation(ClassData classData,
			com.codegenerator.jgen.handler.model.Service service, Controller controller)
			throws ServletRequestBindingException {
		if (!classData.getGenerateRepository() && service.getGenerateService()) {
			throw new JGenGeneratorException("Cannot generate Service without Repository!", classData.getTableName());
		} else if (!classData.getGenerateRepository() && controller.getGenerateController()) {
			throw new JGenGeneratorException("Cannot generate Controller without Repository!",
					classData.getTableName());
		}

		else if (!service.getGenerateService() && controller.getGenerateController()) {
			throw new JGenGeneratorException("Cannot generate Controller without the Service!",
					classData.getTableName());
		} else
			validateMethods(service.getServiceOperations(), controller.getControllerOperations(),
					classData.getTableName());
	}

	private void validateMethods(ServiceOperations so, ControllerOperations co, String tableName) {
		if (co.getGet() && !so.getFindById()) {
			throw new JGenGeneratorException("Cannot generate Controller GET method without Service findById method!", tableName);
		}
		if (co.getPut() && !so.getFindById()) {
			throw new JGenGeneratorException("Cannot generate Controller PUT method without Service findById method!", tableName);
		}
		if (co.getPut() && !so.getSave()) {
			throw new JGenGeneratorException("Cannot generate Controller PUT method without Service save method!", tableName);
		}
		if (co.getPost() && !so.getSave()) {
			throw new JGenGeneratorException("Cannot generate Controller POST method without Service save method!", tableName);
		}
		if (co.getDelete() && !so.getDelete()) {
			throw new JGenGeneratorException("Cannot generate Controller DELETE method without Service delete method!", tableName);
		}
	}

}
