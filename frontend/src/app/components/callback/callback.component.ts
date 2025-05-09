import {Component, inject, OnInit} from '@angular/core';
import {AuthService} from '@services/auth/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {User} from '@models/user';
import {UserService} from '@services/users/user.service';
import {switchMap, tap} from 'rxjs';

@Component({
    selector: 'app-callback',
    imports: [],
    templateUrl: './callback.component.html',
    styleUrl: './callback.component.css'
})
export class CallbackComponent implements OnInit {
  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  router: Router = inject(Router);
  userService: UserService = inject(UserService);
  authService: AuthService = inject(AuthService);
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      const returnedState = params['state'];
      const storedState = sessionStorage.getItem('oauth_state');

      if (returnedState !== storedState) {
        console.error('Échec de vérification du state');
        return;
      }

      this.http.post<any>('http://localhost:8080/api/auth/github/token', { code }, { withCredentials: true })
        .pipe(
          switchMap(response => {
            return this.userService.getUserById(response.user.id).pipe(

              tap((user: User) => {

                if (user == null) {
                  const newUser : User = {
                    id : -1,
                    username: response.user.name,
                    password: null,
                    email: response.user.email,
                    providerId: response.user.id,
                    boards: []
                  }
                  this.userService.createUser(newUser).subscribe({
                    next : (user) => {
                      this.authService.setCurrentUser(user);
                      console.log(user)
                      if(user.id )
                          localStorage.setItem("USER_ID",user.id.toLocaleString())
                      this.router.navigateByUrl(`users/${user.id}/workspaces`);

                    }
                  })
                }


              })
            );
          })
        )
        .subscribe({
          error: (err) => {
            console.error('Erreur lors de l’échange de token ou récupération/création du user :', err);
          }
        });
    });
  }
}

