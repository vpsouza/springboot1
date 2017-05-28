package me.viniciuspiedade.springboot1.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import me.viniciuspiedade.springboot1.model.Product;

public interface ProductValidation extends Function<Product, ValidationResult> {

	static ProductValidation isPayloadDefined() {
		return holds(payload -> payload != null , ValidationResult.Violation.violation("Product payload is not defined", ErrorEnum.PRODUCT_PAYLOAD_UNDEFINED));
	}
	
    static ProductValidation isNameDefined() {
        return holds(payload -> payload.getName().trim().length() > 0, ValidationResult.Violation.violation("Product name is not defined.", ErrorEnum.PRODUCT_NAME_UNDEFINED));
    }
    
    static ProductValidation holds(Predicate<Product> p, ValidationResult.Violation validation){
        return payload -> p.test(payload) ? ValidationResult.valid() : ValidationResult.invalid(validation);
    }

    default ProductValidation and(ProductValidation other) {
        return user -> {
            final ValidationResult result = this.apply(user);
            return result.isValid() ? other.apply(user) : result;
        };
    }
    
    static ProductValidation all(ProductValidation... validations){ 
    	  return payload -> Arrays.stream(validations).map(v -> v.apply(payload)).reduce(ValidationResult.valid(), (acc, b) -> {
    	    if(b.isValid()) return acc;
    	    List<ValidationResult.Violation> violations = new ArrayList<ValidationResult.Violation>(acc.getViolations().get());
    	    violations.addAll(b.getViolations().get());
    	    return new ValidationResult.Invalid(b.getViolations());
    	  });
    }
    
    interface RestValidation {
    	
    	static ProductValidation isProductExists() {
    		return holds(payload -> payload != null && payload.getId() != null && payload.getId() > 0, ValidationResult.Violation.violation("Product does not exists", ErrorEnum.PRODUCT_NOT_EXISTS));
    	}
    	
    	static ProductValidation isCreatePayloadValid() {
            return ProductValidation.all(isPayloadDefined(), isNameDefined())
            		.and(holds(payload -> payload.getId() == null, ValidationResult.Violation.violation("Invalid request body - id must not be defined.", ErrorEnum.PRODUCT_CREATE_PAYLOAD_NOT_WELL_DEFINED)));
        }
    	
    	static ProductValidation isUpdatePayloadValid() {
    		return ProductValidation.all(isPayloadDefined(), isNameDefined())
    				.and(holds(product -> product.getId() > 0, ValidationResult.Violation.violation("Invalid request body - id must be defined.", ErrorEnum.PRODUCT_UPDATE_PAYLOAD_NOT_WELL_DEFINED)));
        }
    }
}
