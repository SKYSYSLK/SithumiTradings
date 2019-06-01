# SITHUMI TRADINGS
## An Inventory Management System

### Introduction
In this project, we built up an inventory management system for a client in *Alawwa city, Kurunegala, Sri Lanka*. The system is capable to handle all the funtionalities which are client expected, such as handling information of shops and items, manage cheques related information, report generating, etc.

<br>

![Capture01](./readme-assets/capture01.jpg)

<br>

![Capture02](./readme-assets/capture02.jpg)

<br>

![Capture01](./readme-assets/capture03.jpg)

<br>

### Project Scope

**IN SCOPE**
- Full featured stand alone PC application for handling inventory information.
  - handling buyers and sellers shop information
  - handling items information related to the shops
  - managing cheques information
  - managing invoices information
- Report generating in PDF format
  - In time and shop based report generating

**OUT SCOPE**
- The system does not concern about the size of the storage devices.

<br>

### Used Technologies

- [Java Language](https://www.java.com/en/)
- [JavaFX platform](https://openjfx.io/)
- [Scene Builder](https://gluonhq.com/products/scene-builder/)
- [SQLite Database Management](https://www.sqlite.org/index.html)
- [PDFbox library](https://pdfbox.apache.org/)
- [Intellij Idea Community Edition](https://www.jetbrains.com/idea/download/#section=linux)

<br>

### Installation Guidance for developers

**PREREQUISITES**

- Java Development Kit 8
- JavaFX version 8
- Intellij Idea Community Edition ( You can choose any development environment as your expectations)

**HOW TO INSTALL**

- Fork and clone the github repository of the project.
- Run following bash commands inside your project directory as the order.

```console
shell:~$ composer install
.........................
.........................
shell:~$ php artisan migrate
.........................
.........................
shell:~$ php artisan storage:link
.........................
.........................
```

- Then add the following code segment into the end of your .env file

```
STRIPE_PUB_KEY=<Your Stripe Account Public Key>
STRIPE_SECRET_KEY=<Your Stripe Account Secret Key>
```

- Finally run the web server and go to the [http://127.0.0.1:8000](http://127.0.0.1:8000)

```console
shell:~$ php artisan serve
```

<br>

### Developers
<table>
<tr>
<td align="center"><img src="https://avatars0.githubusercontent.com/u/25032998?s=460&v=4" width=100></td>
<td align="center"><img src="https://avatars0.githubusercontent.com/u/12469768?s=400&v=4" width=100></td>
<td align="center"><img src="https://avatars0.githubusercontent.com/u/29378743?s=400&v=4" width=100></td>
<td align="center"><img src="https://avatars1.githubusercontent.com/u/25387297?s=400&v=4" width=100></td>
<td align="center"><img src="https://avatars1.githubusercontent.com/u/13849811?s=400&v=4" width=100></td>
</tr>
<tr>
<td align="center"><a href="https://github.com/RavinduSachintha">Ravindu Sachintha</a></td>
<td align="center"><a href="https://github.com/Danushka96">Danushka Herath</a></td>
<td align="center"><a href="https://github.com/Sacheerc/">Sachintha Rathnayake</a></td>
<td align="center"><a href="https://github.com/AsithaIndrajith">Asitha Indrajith</a></td>
<td align="center"><a href="https://github.com/ShehanKule">Shehan Kulathilake</a></td>
</tr>
</table>

<br>

### Contributing
Warmly welcome to developers for contributing **PersoAd** Project. Make sure to open an issue and communicate with us before 
creating a Pull Request.

<br>

### License

The PersoAd System is open-sourced software solution licensed under the [GNU General Public License v3.0](./LICENSE).

