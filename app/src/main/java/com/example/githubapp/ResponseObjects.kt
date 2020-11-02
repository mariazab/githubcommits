package com.example.githubapp

import java.util.*


//Object for the whole commit, the author here is the higher level author that contains the avatar_url but doesn't have name, it may be null
class CommitResponse (val commit: Commit, val html_url: String, val author: Author?)

//Object for commit details where there is info about author and committer etc
class Commit (val author: Author, val committer: Committer, val message: String)

//Object for author that is inside commit details (also used for the higher level author therefore it includes the avatar_url
class Author (val name: String, val email: String, val date: Date, val avatar_url: String = "")

//Object for committer that is inside details
class Committer (val name: String, val email: String, val date: Date)

/*Example response:
    {
        ...
        "commit": {
            "author": {
                "name": "John Doe",
                "email": "john@google.com",
                "date": "2020-10-30T16:52:28Z"
            },
            "committer": {
                "name": "Matt Smith",
                "email": "matt@google.com",
                "date": "2020-10-30T16:52:28Z"
            },
        "message": "First commit",
        ...
        }
        ...
        "html_url": " ",
        "author": {
            ...
            "avatar_url": " ",
            ...
        },
        "committer": null,
        ...
    }
 */