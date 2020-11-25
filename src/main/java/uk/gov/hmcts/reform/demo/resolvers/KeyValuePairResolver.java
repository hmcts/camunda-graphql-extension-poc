package uk.gov.hmcts.reform.demo.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import uk.gov.hmcts.reform.demo.types.KeyValuePair;

import java.util.logging.Logger;

/**
 * Created by daniel on 23.09.17.
 */
public class KeyValuePairResolver implements GraphQLResolver<KeyValuePair> {

    private final static Logger LOGGER = Logger.getLogger(KeyValuePair.class.getName());


}
