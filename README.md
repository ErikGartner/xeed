# XEED
XEED is a tool that organizes role-playing settings. It focuses on story driven games and does not have map support.

## Usage

### Installation
XEED uses maven for building executable jars with bundled dependencies.
```
mvn package
```
It will generate a ```target/``` folder with the compiled jars.

### Running
Depending on the host system either double click on the built jar files or type:
```
java -jar XEED.jar
```

## Tests
None at the moment but they should be stored in ```src/test/java/```.

## Contributions
Pull requests are welcome. Main developer is @erik_gartner.

### Problems
This project is very old and suffers from incorrect code conventions, bad patterns and a tangled structure. Fixing these problem should be considered high priority before developing new features.

For example:
- Variables have the type as prefix.
- Methods start with a capital letter.
- The coupling is very high.
- Potential threading problem.
- Propagating changes between forms is mess.
- Main class is way to big and contains and breaks many OOP principles.

Despite these problem the program is generally stable and extended usage over several years have only revealed minor bugs.

## License
Copyright 2015 Erik Gärtner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
