package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ATv00kuqeFNoI8IPtcugGsEuo9ptPrayo6MDlKcb")
                .clientKey("quY7zDEc5BZ57IptU37iJTkkhxzEdy38cEJtPhCr")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
