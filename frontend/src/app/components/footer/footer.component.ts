import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true, // Si estás usando componentes standalone (sin módulo), debes declararlo así.
  imports: [RouterModule], // Puedes incluir RouterModule si usas [routerLink] en el HTML.
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'] // Corrección: debe ser styleUrls, no styleUrl.
})
export class FooterComponent {

}
