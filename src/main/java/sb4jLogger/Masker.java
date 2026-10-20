package sb4jLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Masker {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Object maskObject(Object value, List<String> targetsToMask) {
        if (value == null)
            return null;
        try {
            return OBJECT_MAPPER.treeToValue(recursiveMask(OBJECT_MAPPER.valueToTree(value), targetsToMask), Object.class);
        } catch (JsonProcessingException e) {
            return "[Masking Error: " + e.getMessage() + "]";
        }
    }

    private JsonNode recursiveMask(JsonNode node, List<String> targetsToMask) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldName = field.getKey();
                JsonNode childNode = field.getValue();
                if (targetsToMask.contains(fieldName))
                    objectNode.put(fieldName, applyUniversalMask(childNode));
                else
                    objectNode.set(fieldName, recursiveMask(childNode, targetsToMask));
            }
            return objectNode;
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++)
                arrayNode.set(i, recursiveMask(arrayNode.get(i), targetsToMask));
            return arrayNode;
        }
        return node;
    }

    private String applyUniversalMask(JsonNode node) {
        return node.isTextual() || node.isNumber() || node.isBoolean() ? UniversalMasker.obfuscate(node.asText()) :"****";
    }
}