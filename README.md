# Auto-Scrabbler
![Validation](https://github.com/Achaaab/auto-scrabbler/actions/workflows/validation.yaml/badge.svg)
![CodeQL](https://github.com/Achaaab/auto-scrabbler/actions/workflows/github-code-scanning/codeql/badge.svg)
![Coverage](.github/badges/jacoco.svg)

<img src="src/main/resources/icon_256.png" width="256" alt="Auto-Scrabbler icon"/>

### Prerequisites
* Java 21+

### Running
* Get the [latest release](https://github.com/Achaaab/auto-scrabbler/releases/latest).

## Screenshots
<img src="data/screenshots/french_duplicate.png" width="1024" alt="gameplay screenshot"/>
<img src="data/screenshots/french_duplicate_solve.png" width="320" alt="about screenshot"/>

## Build native package

### Linux/DEB - AMD64

```shell
mvn clean package

jpackage \
  --name "Auto-Scrabbler" \
  --app-version "0.0.6" \
  --input target \
  --main-jar auto-scrabbler-0.0.6.jar \
  --main-class com.github.achaaab.scrabble.SolverApplication \
  --type deb \
  --dest target \
  --runtime-image /usr/lib/jvm/java-21-openjdk-amd64 \
  --icon src/main/resources/icon_256.png \
  --vendor "Jonathan Guéhenneux"
```

## Authors
* **Jonathan Guéhenneux** - *Programmer* - [Achaaab](https://github.com/Achaaab)

## License
This project is licensed under the GNU General Public License (GPL) - see the [LICENSE.md](LICENSE.md) for the details.
