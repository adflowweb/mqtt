/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.Template;
import kr.co.adflow.pms.domain.TemplateParams;


// TODO: Auto-generated Javadoc
/**
 * The Interface CDRMapper.
 */
public interface TemplateMapper {

	
	/**
	 * insert Template.
	 *
	 * @param params the Template
	 * @return the insert count.
	 */
	int insertTemplate(Template template);
	
	/**
	 * update Template.
	 *
	 * @param params the Template
	 * @return the update count.
	 */
	int updateTemplate(Template template);
	
	
	/**
	 * Select Template.
	 *
	 * @param TemplateParams the templateParams
	 * @return the Template
	 */
	Template selectTemplate(TemplateParams templateParams);
	
	/**
	 * Select Template.
	 *
	 * @param TemplateParams the templateParams
	 * @return the Template
	 */
	List<Template> searchTemplateName(TemplateParams templateParams);
	
	
	/**
	 * delete Template.
	 *
	 * @param TemplateParams the templateParams
	 * @return the delete count
	 */
	int deleteTemplate(TemplateParams templateParams);
	
	/**
	 * Select Template List.
	 *
	 * @param TemplateParams the templateParams
	 * @return the Template
	 */
	List<Template> selectTemplateList(String userId);
	

}
