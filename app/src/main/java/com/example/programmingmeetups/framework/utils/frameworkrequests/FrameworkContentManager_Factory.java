package com.example.programmingmeetups.framework.utils.frameworkrequests;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class FrameworkContentManager_Factory implements Factory<FrameworkContentManager> {
  @Override
  public FrameworkContentManager get() {
    return newInstance();
  }

  public static FrameworkContentManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FrameworkContentManager newInstance() {
    return new FrameworkContentManager();
  }

  private static final class InstanceHolder {
    private static final FrameworkContentManager_Factory INSTANCE = new FrameworkContentManager_Factory();
  }
}
