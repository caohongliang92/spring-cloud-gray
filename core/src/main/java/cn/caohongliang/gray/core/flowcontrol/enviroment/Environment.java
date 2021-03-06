package cn.caohongliang.gray.core.flowcontrol.enviroment;

import cn.caohongliang.gray.core.util.gson.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * 服务的环境和版本信息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Environment {
	private static final ThreadLocal<Environment> threadLocal = new ThreadLocal<>();
	/**
	 * 请求头名称
	 */
	public static final String HEADER_NAME = "x-flow-control-environment";
	/**
	 * 环境名称，英文字符
	 */
	private String name;
	/**
	 * 版本信息，同一个环境中可能有多个版本的代码，如果指定了则只访问指定版本的代码
	 */
	private String version;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Environment that = (Environment) o;
		return Objects.equals(name, that.name) && Objects.equals(version, that.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}

	/**
	 * 将经过Base64的文本转换为EnvironmentVersion对象
	 *
	 * @param base64Text 文本
	 * @return 对象
	 */
	public static Environment decode(String base64Text) {
		if (!StringUtils.hasText(base64Text)) {
			return null;
		}
		try {
			String text = new String(Base64.getDecoder().decode(base64Text), StandardCharsets.UTF_8);
			return GsonUtils.fromJson(text, Environment.class);
		} catch (Exception e) {
			log.error("解码EnvironmentVersion失败：base64Text={}", base64Text);
			return null;
		}
	}

	/**
	 * 缓存到线程中
	 *
	 * @param environment 环境版本信息
	 */
	public static void cache(Environment environment) {
		if (environment == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("缓存环境版本信息：environment={}, version={}", environment.getName(), environment.getVersion());
		}
		threadLocal.set(environment);
	}

	public static Environment get() {
		return threadLocal.get();
	}

	/**
	 * 从线程中清除缓存的环境版本信息
	 */
	public static void clear() {
		Environment environment = threadLocal.get();
		if (environment != null) {
			if (log.isDebugEnabled()) {
				log.debug("清除环境缓存：environment={}, version={}", environment.getName(), environment.getVersion());
			}
		} else {
			log.debug("清除环境缓存：环境版本信息不存在");
		}
		//真正移除
		threadLocal.remove();
	}
}
