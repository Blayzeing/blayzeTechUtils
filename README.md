# blayzeTechUtils
A set of Java utilities for performing additional maths operations, 2D environmental work and more.

**This toolset is currently in the process of being ported to GitHub. Much won't work.**

The classes found in this repository are the results of my work in my free time predominantely in my first year of university.
Initial created, if I remember correctly, for experimentation in (what I didn't realise was called) reinforcement learning it
soon became a general collection of code for me to write to as I explored the Java language and different approaches to OOP
and source control. As such the core tennants of the code structure found here focus on modularity, low-cohesion, high
reusability and sane, readable logic and documenation. This can be seen more in my later work, and less so in my earlier work.

I must've done something useful because I find myself often coming back to these classes, and in particular the 2D engine.
I'm open-sourcing it now so that hopefully someone else may find it useful and because I don't want it to end up in a corner
collecting dust - the last time I used it I was rather surprised to find that I couldn't remember all of it's features.

It's suggested that other git-based projects that use this code include it as a [submodule](https://git-scm.com/book/en/v2/Git-Tools-Submodules).
This is how I do it and I try to retain backwards-compatability, but it also means that a pointer to an older version of the
source will still point at that and not the later version. Feel free to email me if you release a project that uses it, and I'll
add it to the (currently empty - I'm still working to upload other projects!) list :)

## A rundown of what each folder contains:
### agents/
Some rudimentry agent-based learning environment work [**prone to change**]

### blayzeNet/
A rudimentry implementation of some neural network paradigms [**prone to change/deletion**]

### env/
A fairly fleshed-out 2D engine, useful for performing 2D experiments or as a backbone for other graphics projects.

### graphics/
A small collection of graphics classes primarily designed for rendering 2D environments build in **env/**

### math/
A collection of additional math functions and helpers. Fairly fully-featured lightweight vector/matrix library.

### random/
Random generators will be placed in here. Currently contains implementations of 2D Perlin noise - one is fixed 
within a grid, and the other can be expanded "infinitely" - see [my post on dynamic background generation](http://blayze.tech/viewPost.php?id=3]=)
to get behind my thinking for the infinite version.

### tri/
Originally a port of a 3D engine I wrote in AS3 [**incomplete and prone to change/deletion**]
