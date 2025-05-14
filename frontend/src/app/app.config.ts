import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { tokenInterceptor } from './services/token-interceptor.service';

export const appConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([tokenInterceptor])
    )
  ]
};
