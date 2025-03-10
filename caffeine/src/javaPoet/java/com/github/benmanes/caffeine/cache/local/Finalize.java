/*
 * Copyright 2015 Ben Manes. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.benmanes.caffeine.cache.local;

import org.apache.commons.lang3.StringUtils;

import com.palantir.javapoet.AnnotationSpec;

/**
 * @author ben.manes@gmail.com (Ben Manes)
 */
public final class Finalize implements LocalCacheRule {

  @Override
  public boolean applies(LocalCacheContext context) {
    return true;
  }

  @Override
  public void execute(LocalCacheContext context) {
    if (!context.suppressedWarnings.isEmpty()) {
      var format = (context.suppressedWarnings.size() == 1)
          ? "$S"
          : "{" + StringUtils.repeat("$S", ", ", context.suppressedWarnings.size()) + "}";
      context.cache.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
          .addMember("value", format, context.suppressedWarnings.toArray())
          .build());
    }
    context.cache.addMethod(context.constructor.build());
  }
}
