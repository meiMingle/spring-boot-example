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

package com.livk.ck.r2dbc.repository;

import com.livk.ck.r2dbc.entity.User;
import com.livk.commons.util.DateUtils;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;

/**
 * <p>
 * Repository
 * </p>
 *
 * @author livk
 */
@Repository
public class UserRepository {

    private final Mono<? extends Connection> mono;

    public UserRepository(ConnectionFactory connectionFactory) {
        this.mono = Mono.from(connectionFactory.create());
    }

    public Flux<User> findAll() {
        return mono.flatMapMany(connection ->
                        connection.createStatement("select id, app_id, version, reg_time from user").execute())
                .flatMap(result -> result.map(User::collect));
    }

    public Mono<Void> deleteById(Mono<Integer> id) {
        return id.flatMap(i ->
                mono.flatMapMany(connection ->
                        connection.createStatement("alter table user delete where id=:id")
                                .bind("id", i)
                                .execute()
                ).then()
        );
    }

    public Mono<Void> save(User user) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.YMD);
        String time = formatter.format(user.getRegTime());
        return mono.flatMapMany(connection ->
                connection.createStatement("insert into user values (:id,:appId,:version,:regTime)")
                        .bind("id", user.getId())
                        .bind("appId", user.getAppId())
                        .bind("version", user.getVersion())
                        .bind("regTime", time)
                        .execute()
        ).then();
    }
}
