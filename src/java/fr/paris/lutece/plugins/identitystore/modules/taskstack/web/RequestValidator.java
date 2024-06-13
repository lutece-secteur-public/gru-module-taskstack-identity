package fr.paris.lutece.plugins.identitystore.modules.taskstack.web;

import fr.paris.lutece.plugins.identitystore.business.identity.Identity;
import fr.paris.lutece.plugins.identitystore.business.identity.IdentityHome;
import fr.paris.lutece.plugins.identitystore.cache.IdentityAttributeValidationCache;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.DtoConverter;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.TaskType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class RequestValidator {

    private final IdentityAttributeValidationCache attrValidationCache = SpringContextService.getBean("identitystore.identityAttributeValidationCache");

    private static RequestValidator _instance;

    public static RequestValidator instance() {
        if (_instance == null) {
            _instance = new RequestValidator();
        }
        return _instance;
    }

    private RequestValidator() {
    }

    public void validateTargetIdentity(final String cuid, final String taskType) throws IdentityStoreException {
        final Identity identity = IdentityHome.findByCustomerId(cuid);
        if (identity == null) {
            throw new IdentityNotFoundException("No identity was found for the provided CUID");
        }
        if (identity.isDeleted()) {
            throw new IdentityStoreException("The identity is deleted");
        }
        if (identity.isMerged()) {
            throw new IdentityStoreException("The identity is merged");
        }

        final IdentityDto identityDto = DtoConverter.convertIdentityToDto(identity);
        switch (TaskType.valueOf(taskType)) {
            case EMAIL_VALIDATION_REQUEST: validateEmail(identityDto, true);
            case ACCOUNT_CREATION_REQUEST: validateAccountRequirement(identityDto);
            default: throw new IdentityStoreException("Invalid task type: " + taskType);
        }
    }

    private void validateEmail(final IdentityDto identityDto, final boolean checkCertification) throws IdentityStoreException {
        final AttributeDto emailAttr = identityDto.getAttributes().stream().filter(a -> a.getKey().equals(Constants.PARAM_EMAIL)).findFirst()
                                                  .orElseThrow(() -> new IdentityStoreException("The identity does not have an email attribute"));
        if (!attrValidationCache.get(Constants.PARAM_EMAIL).matcher(emailAttr.getValue()).matches()) {
            throw new IdentityStoreException("The identity email has an invalid value format");
        }
        if(checkCertification){
            if (emailAttr.getCertificationLevel() != null && emailAttr.getCertificationLevel() > 100) {
                throw new IdentityStoreException("The identity email has already been validated");
            }
        }
    }

    private void validateAccountRequirement(final IdentityDto identityDto) throws IdentityStoreException {
        validateEmail(identityDto, false);
        if(identityDto.isMonParisActive()) {
            throw new IdentityStoreException("The identity is already connected");
        }

        // TODO l'identité doit avoir un niveau minimum de certification (à définir en properties, le niveau de ORIG1 pour commencer)
        final String minCertificationCode = AppPropertiesService.getProperty("task.account.creation.minimum.certification");
    }
}
