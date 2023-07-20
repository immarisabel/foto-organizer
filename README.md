# PixieSort

PixieSort is a simple and easy-to-use photo organizer app.
Its primary purpose is to sort and organize your photos based on their original taken dates.
With PixieSort, you can quickly find and group your memories by when they were captured, making it a breeze to relive those special moments in chronological order. 
Say goodbye to photo clutter, and let PixieSort work its enchantment on your pictures!

# Requirements:

- copy the pictures, not move. So that the originals remain untouched in case of errors.
- update created and modified time to taken time of the picture
- modify the file name to the created timestamp
- created folders according to year and month (YYYY/MM)
- skip anything that is not pictures jpg and png
- detect duplicates and skip them

- for whatsapp pictures, retain the file name and location as it is

# How to use:
### To adjust WhatsApp metadata
Images for WhatsApp need to be named: IMG-YYYYMMDD-WAXXXX.jpg

Only valid for direct pictures on the WhatsApp Media / Pictures folder

### To organize normal pictures which are taken by camera (no matter which)
- you can create manually a copy just to be on the safe side, in case things don't go well, but program should make copies too and not touch originals
- give the path where the pictures are (it will include the sub-folders)
- give the path where you want them to be saved
- click organize

# Still to do:

- [ ] header rendering. Almost impossible after 2 hours with all possible ways I could think of.
- [ ] backup not being created, only backup folder
- [ ] for some reason some images are skipped but i cant find pattern as of why
- [ ] whatsapp ones so far so good
- [ ] warning to be added when field for folder is empty and you still press. currently says successful.
- [ ] warning to be added when backup is created: not displaying yet as it does not create yet
- [ ] warnings to be all displayed INSIDE UI not a popup.