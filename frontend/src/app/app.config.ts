import {ApplicationConfig, LOCALE_ID, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import {
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideOAuthClient} from 'angular-oauth2-oidc';
import {AuthInterceptor} from '@app/auth-interceptor';
import {DatePipe} from '@angular/common';
import {provideNativeDateAdapter} from '@angular/material/core';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([AuthInterceptor])),
    provideAnimationsAsync(),
    provideOAuthClient(),
    provideNativeDateAdapter(),
    DatePipe,
    { provide: LOCALE_ID, useValue: 'fr-FR' },
  ]
};
