package de.blacktri.restapi.pojos;

import org.springframework.util.StringUtils;

import java.security.InvalidParameterException;
import java.util.Map;

public class BlackTriObject {
  protected void addOptionalParameter(Map<String, Object> map, String name, Object value) {
    if (value instanceof String) {
      if (!StringUtils.isEmpty(value)) {
        map.put(name, value);
      }
    }
  }

  protected void addRequiredParameter(Map<String, Object> map, String name, Object value) {
    if (value instanceof String) {
      if (StringUtils.isEmpty(value)) {
        throw new InvalidParameterException("Name needs to be set");
      }
      map.put(name, value);
    } else {
      throw new InvalidParameterException("Name needs to be set");
    }
  }
}
