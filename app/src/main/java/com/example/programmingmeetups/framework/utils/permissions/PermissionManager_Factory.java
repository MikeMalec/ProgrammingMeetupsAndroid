package com.example.programmingmeetups.framework.utils.permissions;

import android.content.Context;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class PermissionManager_Factory implements Factory<PermissionManager> {
  private final Provider<Context> contextProvider;

  public PermissionManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PermissionManager get() {
    return newInstance(contextProvider.get());
  }

  public static PermissionManager_Factory create(Provider<Context> contextProvider) {
    return new PermissionManager_Factory(contextProvider);
  }

  public static PermissionManager newInstance(Context context) {
    return new PermissionManager(context);
  }
}
