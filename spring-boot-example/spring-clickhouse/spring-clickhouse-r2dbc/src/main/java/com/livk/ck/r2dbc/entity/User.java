/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.ck.r2dbc.entity;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
@Accessors(chain = true)
@Table("user")
public class User {

	@Id
	private Integer id;

	private String appId;

	private String version;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regTime;

	public static User collect(Row row, RowMetadata rowMetadata) {
		return new User();
	}

}
