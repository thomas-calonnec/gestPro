import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {OAuthService} from 'angular-oauth2-oidc';

@Component({
    selector: 'app-root',
    imports: [RouterOutlet, FormsModule, FontAwesomeModule],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss',
    providers: []
})
export class AppComponent  {
  constructor(private oauthService: OAuthService) {

    // URL of the SPA to redirect the user to after login
    this.oauthService.redirectUri = window.location.origin + "/";

    // The SPA's id. The SPA is registerd with this id at the auth-server
    this.oauthService.clientId = "Ov23liGBc9wuOQ9SDN8a";

    // set the scope for the permissions the client should request
    // The first three are defined by OIDC. The 4th is a usecase-specific one
    this.oauthService.scope = "openid profile email voucher";

    // set to true, to receive also an id_token via OpenId Connect (OIDC) in addition to the
    // OAuth2-based access_token
    this.oauthService.oidc = true; // ID_Token

    // Use setStorage to use sessionStorage or another implementation of the TS-type Storage
    // instead of localStorage
    this.oauthService.setStorage(sessionStorage);


    // // Load Discovery Document and then try to login the user
    // this.oauthService.loadDiscoveryDocument(url).then(() => {
    //
    //   // This method just tries to parse the token(s) within the url when
    //   // the auth-server redirects the user back to the web-app
    //   // It dosn't send the user the the login page
       this.oauthService.tryLogin().then();
    //
    // });

  }


}
