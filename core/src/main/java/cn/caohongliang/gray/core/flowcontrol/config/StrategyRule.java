package cn.caohongliang.gray.core.flowcontrol.config;

import cn.caohongliang.gray.core.flowcontrol.enviroment.RequestWrapper;

/**
 * 策略规则
 *
 * @author caohongliang
 */
public interface StrategyRule {

	/**
	 * 校验规则是否正确
	 *
	 * @return true=规则正常
	 */
	boolean validate();

	/**
	 * 校验该请求是否匹配该规则
	 *
	 * @param request 请求
	 * @return true=匹配
	 */
	boolean match(RequestWrapper request);
}
