package com.crazygis.security.springsecurity;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 进行权限验证
 *
 * Created by xuguolin on 2017/3/18.
 */
public class CrazygisCustomAccessDecisionManager implements AccessDecisionManager {
    private List<AccessDecisionVoter<? extends Object>> decisionVoters;
    private boolean allowIfAllAbstainDecisions = false;

    public CrazygisCustomAccessDecisionManager() {
        decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();

        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        decisionVoters.add(roleVoter);

        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        decisionVoters.add(authenticatedVoter);
    }

    public boolean isAllowIfAllAbstainDecisions() {
        return allowIfAllAbstainDecisions;
    }

    public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
        this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        int deny = 0;

        for (AccessDecisionVoter voter : this.decisionVoters) {
            int result = voter.vote(authentication, object, configAttributes);

            switch (result) {
                case AccessDecisionVoter.ACCESS_GRANTED:
                    return;

                case AccessDecisionVoter.ACCESS_DENIED:
                    deny++;

                    break;

                default:
                    break;
            }
        }

        if (deny > 0) {
            throw new AccessDeniedException("Access is denied");
        }

        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        for (AccessDecisionVoter voter : this.decisionVoters) {
            if (voter.supports(attribute)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        for (AccessDecisionVoter voter : this.decisionVoters) {
            if (!voter.supports(clazz)) {
                return false;
            }
        }

        return true;
    }

    protected final void checkAllowIfAllAbstainDecisions() {
        if (!this.isAllowIfAllAbstainDecisions()) {
            throw new AccessDeniedException("Access is denied");
        }
    }
}
