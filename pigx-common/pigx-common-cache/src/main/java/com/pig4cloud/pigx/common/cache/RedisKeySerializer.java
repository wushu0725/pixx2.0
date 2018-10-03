/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.common.cache;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 将redis key序列化为字符串
 *
 * <p>
 *     spring cache中的简单基本类型直接使用 StringRedisSerializer 会有问题
 * </p>
 *
 * @author L.cm
 */
public class RedisKeySerializer implements RedisSerializer<Object> {
	private final Charset charset;
	private final ConversionService converter;

	public RedisKeySerializer() {
		this(StandardCharsets.UTF_8);
	}

	public RedisKeySerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
		this.converter = DefaultConversionService.getSharedInstance();
	}

	@Override
	public Object deserialize(byte[] bytes) {
		throw new RuntimeException("Used only for serializing key, not for deserialization.");
	}

	@Override
	@Nullable
	public byte[] serialize(@Nullable Object object) {
		if (object == null) {
			return null;
		}
		String key;
		if (object instanceof SimpleKey) {
			key = "";
		} else if (object instanceof String) {
			key = (String) object;
		} else {
			key = converter.convert(object, String.class);
		}
		return key.getBytes(this.charset);
	}

}
