# TrainingSim
A textbased erotic game

Since I have some free time at the moment I started to develop a little game in Java. 
I choose java because of the programming langueages I know so far I like to work with it the most.

What's the games purpose?
For once I wanted to do a text game for a long time. 
I also enjoyed playing some text based erotic adventures like TITS, COC and FOE a lot, 
but I fantasized about how these games mechanics could be changed in a way that would 
address some of my preferences better. One of these is anal training. The idea of 
striving towards taking bigger toys, (or in these scenarios mostly unrealistically big cocks). 
For me the idea of slow progression is part of what makes it exciting, but in these games instead 
it is mostly pretty easy to reach the maximal step of stretching. It doesn't take effort, 
because than most of the prewritten scenes centered around oversized cocks would be too hard to reach. 

However I intend to build the game in a way that also allows the mechanics to represent other cavities and orifices, 
like a vagina or an urethra or even a nipple or something if there would be popular demand, I guess.

This is just a small scale project and should actually be pretty well suited to become part of a bigger project later. 

For now I have some basic concepts.
The view is a crappy GUI I build with swing, and for now just a tool for myself to test things. 

By now the following concepts are implemented, but might be changed, adjusted or replaced:

cavities
- a cavity could be an anus, a vagina, an urethra, a cloaca... etc.
- cavities have segments, each is 1cm in length 
- segments have low, current and high capacities, that tell about to which diameter they can be stretched 
  until certain effects take place
- cavities have soreness, looseness and pain values
- soreness accelerates the pain production

toys
- atm. toys have a length and a diameter, but later it will be possible for them to have segments with different widths, 
allowing the simulation of knots, flares, bulges, or butt-plug-typical shapes

penetration
- taking penetration is a capability of cavities
- the inserted object is actually moved through the cavity, sort of simulating actual penetration 
(that will mostly be important once there are toys with actual shapes defined by a changing diameter for their length)
- for each segment and toy position the following occurs:
- if the diameter of the toy in that segment is
	higher than any of the capacities: said capacity will slowly adapt to the toys size
	higher than the current capacity and low capacity of that segment: soreness and pain are produced
	higher than the high capacity of that segment: a whole lot of pain and soreness are produced

- every time the inserted object changes position the following occurs:
	the current capacity of each segment slightly declines
	pain and soreness decline somewhat
	very high levels of soreness produce little amounts of pain

if the cavity is unable to take the whole length of the toy (deeper parts start out tighter) 
the segment following the last (too tight) segment's current capacity accommodates to the 
current capacity of the last segment (so if you use a toy a lot you will manage to take it deeper) 
every time the toy hits the last segment

if the character becomes too sore or the pain threshold is reached, the player has to take a rest for a while, before they can keep training

Last Update:
- a new GUI createt with swing & netbeans. Note that the options Panel doesn't has any functionality yet, but these will be added soon.
- drastically improved a lot of mechanics, so that improving the balancing will be a lot easier in the future
- this still doesn't have a lot of gameplay, I'm sorry!

Planned for the near future:
- allowing to choose a character name, pronouns and what cavity shall be trained, use that information in the output (third person description)
- creating a simple character class, for now with a sleep variable/schedule, so the player won't be able to toy like 24/7


