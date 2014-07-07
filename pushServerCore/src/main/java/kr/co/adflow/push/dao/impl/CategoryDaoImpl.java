package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.CategoryDao;
import kr.co.adflow.push.domain.Category;
import kr.co.adflow.push.mapper.CategoryMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 7. 7.
 * 
 */
@Repository
public class CategoryDaoImpl implements CategoryDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(CategoryDaoImpl.class);

	@Autowired
	private SqlSession sqlSession;

	@Override
	public Category get(int categoryID) throws Exception {
		logger.debug("get시작(categoryID=" + categoryID + ")");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		Category category = categoryMapper.get(categoryID);
		logger.debug("get종료(" + category + ")");
		return category;
	}

	@Override
	public int post(Category category) throws Exception {
		logger.debug("post시작(category=" + category + ")");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		int count = categoryMapper.post(category);
		logger.debug("post종료(count=" + count + ")");
		return count;
	}

	@Override
	public int delete(int categoryID) throws Exception {
		logger.debug("delete시작(categoryID=" + categoryID + ")");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		int result = categoryMapper.delete(categoryID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	@Override
	public Category[] getAllCategories() throws Exception {
		logger.debug("getAllCategories시작()");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		Category[] category = categoryMapper.getAllCategories();
		logger.debug("getAllCategories종료(category=" + category + ")");
		return category;
	}

}
