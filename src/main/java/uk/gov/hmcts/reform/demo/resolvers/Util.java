package uk.gov.hmcts.reform.demo.resolvers;

import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.ObjectValueImpl;
import org.camunda.bpm.engine.variable.type.SerializableValueType;
import org.camunda.bpm.engine.variable.value.TypedValue;
import uk.gov.hmcts.reform.demo.types.KeyValuePair;
import uk.gov.hmcts.reform.demo.types.ValueTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.camunda.spin.Spin.JSON;

public class Util {

    private final static Logger LOGGER = Logger.getLogger(Util.class.getName());

    static public void switchContext(RepositoryService repositoryService, String pdid, ProcessEngineConfigurationImpl processEngineConfiguration) {

        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(pdid);
        String deploymentId = processDefinition.getDeploymentId();
        ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();
        ProcessApplicationReference targetProcessApplication = processApplicationManager.getProcessApplicationForDeployment(deploymentId);

        if (targetProcessApplication != null) {
            String processApplicationName = targetProcessApplication.getName();
            ProcessApplicationContext.setCurrentProcessApplication(processApplicationName);
        }
    }

    public static List<KeyValuePair> getKeyValuePairs(VariableMap variableMap) {

        ArrayList<KeyValuePair> keyValuePairs = new ArrayList<>();

        for (VariableMap.Entry<String, Object> i : variableMap.entrySet()) {

            Object objValue = i.getValue();
            String key = i.getKey();
            TypedValue typedValue = variableMap.getValueTyped(key);

            if (objValue != null) {
                String value;
                if (typedValue.getType() == SerializableValueType.OBJECT) {

                    ObjectValueImpl objectValueImpl = ((ObjectValueImpl) typedValue);
                    if (objectValueImpl.getSerializationDataFormat().equals(Variables.SerializationDataFormats.JSON.toString())) {
                        value = JSON(key).toString();
                    } else {
                        value = typedValue.getValue().toString();
                    }

                } else {
                    value = objValue.toString();
                }

                KeyValuePair keyValuePair = new KeyValuePair(
                    key,
                    value,
                    ValueTypeEnum.from(variableMap.getValueTyped(key).getType()));
                keyValuePairs.add(keyValuePair);
            } else {
                LOGGER.info("objValue is null: " + key);
            }
        }
        return keyValuePairs;
    }
}
