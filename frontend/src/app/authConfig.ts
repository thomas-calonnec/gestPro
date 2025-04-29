import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  loginUrl: 'https://github.com/login/oauth/authorize',
  redirectUri: window.location.origin + '/callback',
  clientId: 'Ov23liGBc9wuOQ9SDN8a', // <-- à remplacer
  responseType: 'code',
  scope: 'read:user user:email',
  oidc: false, // GitHub ne supporte pas OpenID Connect
  showDebugInformation: true,
  requireHttps: false,
};
