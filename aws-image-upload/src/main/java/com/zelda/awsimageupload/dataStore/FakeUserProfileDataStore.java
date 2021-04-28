package com.zelda.awsimageupload.dataStore;

import com.zelda.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("5809510e-8e44-43d2-aaba-1481102272aa"), "Diego", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("ac9697a3-be0d-400b-b547-0f0a42c1615b"), "Avery", null));
    }

    public static List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
