TFC Seed Maker
==============

Purpose
-------

There are 2 main reasons I'm making this:

1. I wanted to be able to check one or more existing seeds for resoureces and/or make maps of those resources.
2. I wanted to make a non-spoiling way to select a good seed. (aka be sure it has some decent resources within a reasonable radius of spawn, without knowing where to go look)

This program spits out json files, which can then be analyzed by hand or by an external program.
I'm now working on some JavaScript (NodeJS) program to present the results and allow for custom filtering.

License
-------

&copy; 2015 - Dries007

Unless otherwise specified per file, or per package via the package-info.java file.

This program contains code from the [Terrafirmacraft mod](https://github.com/Deadrik/TFCraft), hence the GPL license.

Done/Todo
---------

- [x] Make maps per layer + combined
- [x] Multithreading
- [x] Biomes
- [x] Rock types
- [x] Trees
- [x] Json output of discovered information
- [x] EVT
- [x] Rain
- [x] Stability
- [x] pH
- [x] Drainage
- [x] Add Gradle file and use it for dependecies
- [ ] Add "hardcoded" tree generation (nearly impossible)
- [ ] Crops (nearly impossible)
- [ ] Animals (nearly impossible)
