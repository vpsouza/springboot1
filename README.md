# SpringBoot1

A Sample Spring Boot Restfull project with Spring Data JPA and custom validation exploring Java Lambda with Functional Interfaces. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### Prerequisities

1 - NodeJS: sudo apt-get install nodejs

2 - npm: sudo apt-get install npm

3 - Docker


See the installed versions:

```
node -v

```

```
docker version

Client:
 Version:      1.13.1
 API version:  1.26
 Go version:   go1.7.5
 Git commit:   092cba3
 Built:        Wed Feb  8 08:47:51 2017
 OS/Arch:      darwin/amd64

Server:
 Version:      1.13.1
 API version:  1.26 (minimum version 1.12)
 Go version:   go1.7.5
 Git commit:   092cba3
 Built:        Wed Feb  8 08:47:51 2017
 OS/Arch:      linux/amd64
 Experimental: true
```

```
docker-compose version

docker-compose version 1.11.1, build 7c5d5e4
docker-py version: 2.0.2
CPython version: 2.7.12
OpenSSL version: OpenSSL 1.0.2j  26 Sep 2016
```

### Installing

A step by step series of examples that tell you have to get a development env running

Install docker (Oficial website for docker)

```
   https://docs.docker.com/engine/installation/linux/ubuntu/
```

### Prerequisities
add easycustomer-api.easyapps.local to your /etc/hosts file

### Running the app

```
docker-compose up -d
```

Then make a post to http://easycustomer-api.easyapps.local/api/initDB to configure your database and you are free to call 
the app through http://easycustomer-api.easyapps.local/api/<some-endpoint>

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* Dropwizard - Bla bla bla
* Maven - Maybe
* Atom - ergaerga

## Contributing

Please read 
[CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426)
 for details on our code of conduct, and the process for submitting 
 pull requests to us.

## Versioning

We use [Git](http://www.bitbucket.org) for versioning. For the versions 
available, see the 
[tags on this repository](https://bitbucket.org/easyappscloud/easycustomer). 

## Authors

* **Andre Felix** - *Initial work* - @ [bitBucket](https://bitbucket.org/easyappscloud/easycustomer)

See also the list of [contributors](https://bitbucket.org/easyappscloud/profile/members) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

TODO:
 Add WildFly-addResouce 
CLI Jboss Add resources

Create the FinancialWeb
	Accounts payable
	Receivables accounts
- User CRUD
- Front end