import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.scss']
})
export class AppHeaderComponent implements OnInit {

  img_ip!: string;

  constructor() { }

  ngOnInit(): void {
    this.img_ip = "./assets/img/logoIP.png";
  }

}
