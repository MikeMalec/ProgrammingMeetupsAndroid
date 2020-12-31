package com.example.programmingmeetups.framework.utils.files;

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
public final class FileManager_Factory implements Factory<FileManager> {
  private final Provider<Context> contextProvider;

  public FileManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FileManager get() {
    return newInstance(contextProvider.get());
  }

  public static FileManager_Factory create(Provider<Context> contextProvider) {
    return new FileManager_Factory(contextProvider);
  }

  public static FileManager newInstance(Context context) {
    return new FileManager(context);
  }
}
