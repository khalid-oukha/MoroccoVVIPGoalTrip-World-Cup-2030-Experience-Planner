package com.moroccanvviptrip.api.mvtapi.utils.validator;

import com.moroccanvviptrip.api.mvtapi.utils.annotation.Unique;
import com.moroccanvviptrip.api.mvtapi.web.exception.UniqueConstraintViolationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueFieldValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private String fieldName;
    private Class<?> entityClass;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entityClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String queryString = String.format(
                "SELECT COUNT(e) FROM %s e WHERE e.%s = :value",
                entityClass.getSimpleName(),
                fieldName
        );
        Query query = entityManager.createQuery(queryString);
        query.setParameter("value", value);

        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            throw new UniqueConstraintViolationException(
                    String.format("The value for '%s' must be unique, but '%s' already exists.", fieldName, value)
            );
        }

        return true;
    }

}
