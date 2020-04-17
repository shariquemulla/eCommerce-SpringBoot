package com.ecommerce.config;

import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductCategory;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{

	private EntityManager entityManager;
	
	@Autowired
	public MyDataRestConfig(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		HttpMethod[] unsupportedMethods = {HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.POST};
		
		config.getExposureConfiguration()
			.forDomainType(Product.class)
			.withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods))
			.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods));
		
		config.getExposureConfiguration()
			.forDomainType(ProductCategory.class)
			.withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods))
			.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods));
		
		exposeIds(config);
	}
	
	private void exposeIds(RepositoryRestConfiguration config) {
		Class<?>[] domainTypes = entityManager.getMetamodel()
				.getEntities()
				.stream()
				.map(EntityType::getJavaType)
				.collect(Collectors.toList())
				.stream()
				.toArray(Class[]::new);
		
		config.exposeIdsFor(domainTypes);
	}
	
}
