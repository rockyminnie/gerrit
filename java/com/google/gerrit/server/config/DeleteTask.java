// Copyright (C) 2014 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.server.config;

import static com.google.gerrit.common.data.GlobalCapability.KILL_TASK;
import static com.google.gerrit.common.data.GlobalCapability.MAINTAIN_SERVER;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import com.google.gerrit.extensions.annotations.RequiresAnyCapability;
import com.google.gerrit.extensions.common.Input;
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.server.git.WorkQueue.Task;
import com.google.inject.Singleton;

@Singleton
@RequiresAnyCapability({KILL_TASK, MAINTAIN_SERVER})
public class DeleteTask implements RestModifyView<TaskResource, Input> {

  @Override
  public Response<?> apply(TaskResource rsrc, Input input) {
    Task<?> task = rsrc.getTask();
    boolean taskDeleted = task.cancel(true);
    return taskDeleted
        ? Response.none()
        : Response.withStatusCode(SC_INTERNAL_SERVER_ERROR, "Unable to kill task " + task);
  }
}
