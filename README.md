# owllistener
Listen for multicast packets from owl network energy monitor and send to initialstate

[![Build Status](https://travis-ci.org/cartwrightian/owllistener.svg?branch=master)](https://travis-ci.org/cartwrightian/owllistener)

# Build
>gradle debianPackage

Copy resulting package from build/distributions/owllistener_0.1-1_all.deb to your target machine

# Example install
>sudo dpkg -i owllistener_0.1-1_all.deb

>sudo cp /opt/owllistener/etc/owllistener.conf.example /etc/default/owllistener

>sudo vi /etc/default/owllistener

Insert your initialstate bucket and access key into the file

>sudo update-rc.d owllistener defaults

