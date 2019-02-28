package com.rumango.median.iso.serviceimpl;

import org.springframework.stereotype.Service;

import com.rumango.median.iso.service.ValidationLogic;

@Service
public class ValidationLogicImpl implements ValidationLogic {

//	@Autowired
//	private GenericRepository genericRepo;

	private final String rule_default = "default";
	private final String rule_query = "query";
	private final String rule_lov = "lov";
	private final String rule_condition = "condition";
	private final String rule_wildcard = "wildcard";

	private String ruleType = "default";
//	private int external_system_id, node_map_id, tag_map_id, from_system_id = 3, to_system_id = 2;
//	private String ext_sys_code = "NAV789";

	@Override
	public String validate(int fieldNo, String currentValue) {
		if (ruleType.equalsIgnoreCase(rule_default)) {
			return currentValue;
		} else if (ruleType.equalsIgnoreCase(rule_query)) {
			return currentValue;
		} else if (ruleType.equalsIgnoreCase(rule_lov)) {
			return currentValue;
		} else if (ruleType.equalsIgnoreCase(rule_condition)) {
			return currentValue;
		} else if (ruleType.equalsIgnoreCase(rule_wildcard)) {
			return currentValue;
		} else
			return null;
	}
}
