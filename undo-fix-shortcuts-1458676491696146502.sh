#!/bin/bash
dconf write "/org/gnome/desktop/wm/keybindings/toggle-shaded" "['<Control><Alt>s']"
dconf write "/org/gnome/settings-daemon/plugins/media-keys/screensaver" ""
dconf write "/org/gnome/settings-daemon/plugins/media-keys/terminal" ""
dconf write "/org/gnome/desktop/wm/keybindings/switch-to-workspace-down" "['<Control><Alt>Down']"
dconf write "/org/gnome/desktop/wm/keybindings/switch-to-workspace-up" "['<Control><Alt>Up']"
dconf write "/org/gnome/desktop/wm/keybindings/switch-to-workspace-left" "['<Control><Alt>Left']"
dconf write "/org/gnome/desktop/wm/keybindings/switch-to-workspace-right" "['<Control><Alt>Right']"
dconf write "/org/gnome/desktop/wm/keybindings/begin-move" "['<Alt>F7']"
dconf write "/org/gnome/desktop/wm/keybindings/begin-resize" "['<Alt>F8']"