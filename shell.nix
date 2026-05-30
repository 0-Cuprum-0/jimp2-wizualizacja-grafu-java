{ pkgs ? import <nixpkgs> {} }:
  pkgs.mkShell {
    # nativeBuildInputs is usually what you want -- tools you need to run
    nativeBuildInputs = with pkgs.buildPackages; [  
    jdk17
    tree
	gtk3
        gdk-pixbuf
        adwaita-icon-theme
        hicolor-icon-theme
        shared-mime-info
	librsvg
	gcc
	gnumake
    ];
    shellHook = ''
    	export _JAVA_AWT_WM_NONREPARENTING=1
	export GDK_BACKEND=x11
        export XDG_DATA_DIRS=${pkgs.gtk3}/share:$XDG_DATA_DIRS
	'';

}
