package com.example.poster1019;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The one place that owns the app's movie data: all posters, the selection, and the favorites.
 */
public class PosterRepository {

    // Notified whenever favorites are added or removed (Favorite List, badge).
    public interface FavoritesListener{
        void onFavoritesChanged();
    }

    private static PosterRepository instance;

    private final List<Posters> allPosters;
    private final FavoritesStore favoritesStore;
    private final List<FavoritesListener> favoritesListeners = new ArrayList<>();

    // Creates the single instance on first use. It keeps the application context,
    // never an Activity: holding an Activity in a singleton would leak it.
    public static synchronized PosterRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PosterRepository(context.getApplicationContext());
        }
        return instance;
    }

    private PosterRepository(Context appContext) {
        allPosters = Collections.unmodifiableList(createPosters());
        favoritesStore = new FavoritesStore(appContext);

        // Forget saved favorites whose poster no longer exists in the app.
        List<String> validIds = new ArrayList<>();
        for (Posters poster : allPosters) {
            validIds.add(poster.id);
        }
        favoritesStore.retainOnly(validIds);
    }

    public List<Posters> getAllPosters(){
        return allPosters;
    }

    public Posters findById(String id){
        for(Posters posters:allPosters){
            if(posters.id.equals(id)){
                return posters;
            }
        }
        return null;
    }

    // deselects every poster
    public void clearSelection(){
        for(Posters poster : allPosters){
            poster.isSelected = false;
        }
    }

    //get favorite posters, in the order they were added
    public List<Posters> getFavorites(){
        List<Posters> favorites = new ArrayList<>();
        for (String id : favoritesStore.getIds()) {
            Posters poster = findById(id);
            if (poster != null) {
                favorites.add(poster);
            }
        }
        return favorites;
    }

    public int getFavoriteCount(){
        return favoritesStore.getIds().size();
    }

    // Adds posters to favorites, skipping ones already there.
    // Returns how many were new, so the page can show the right message.
    public int addFavorites(List<Posters> posters) {
        List<String> ids = new ArrayList<>();
        for (Posters poster : posters) {
            ids.add(poster.id);
        }
        int added = favoritesStore.addAll(ids);
        if (added > 0) {
            notifyFavoritesChanged();
        }
        return added;
    }

    // Removes one favorite. Returns true if it was a favorite.
    public boolean removeFavorite(String id) {
        boolean removed = favoritesStore.remove(id);
        if (removed) {
            notifyFavoritesChanged();
        }
        return removed;
    }

    // The listener (observer) pattern: pages register to hear about changes.
    // Whoever adds a listener must remove it again (onDestroyView / onDestroy).
    public void addFavoritesListener(FavoritesListener listener) {
        if (!favoritesListeners.contains(listener)) {
            favoritesListeners.add(listener);
        }
    }

    public void removeFavoritesListener(FavoritesListener listener) {
        favoritesListeners.remove(listener);
    }

    private void notifyFavoritesChanged() {
        for(FavoritesListener listener : new ArrayList<>(favoritesListeners)){
            listener.onFavoritesChanged();
        }
    }


    /**
     * Creates the five custom movie posters displayed on the Main Page.
     * Each movie includes its image, title, rating, director, and description.
     */
    private static List<Posters> createPosters() {
        List<Posters> posters = new ArrayList<>();

        // Rango movie information
        Posters rango = new Posters();
        rango.id = "rango";
        rango.image = R.drawable.poster_rango;
        rango.name = "Rango";
        rango.rating = 5f;
        rango.createdBy = "Gore Verbinski";
        rango.story = "A pet chameleon becomes the unlikely sheriff of a Wild West town.";
        posters.add(rango);

        // Ratatouille movie information
        Posters ratatouille = new Posters();
        ratatouille.id = "ratatouille";
        ratatouille.image = R.drawable.poster_ratatouille;
        ratatouille.name = "Ratatouille";
        ratatouille.rating = 5f;
        ratatouille.createdBy = "Brad Bird";
        ratatouille.story = "A rat with a passion for cooking dreams of becoming a chef in Paris.";
        posters.add(ratatouille);

        // Nacho Libre movie information
        Posters nachoLibre = new Posters();
        nachoLibre.id = "nacho_libre";
        nachoLibre.image = R.drawable.poster_nacho_libre;
        nachoLibre.name = "Nacho Libre";
        nachoLibre.rating = 5f;
        nachoLibre.createdBy = "Jared Hess";
        nachoLibre.story = "A monastery cook becomes a masked wrestler to help support the orphans.";
        posters.add(nachoLibre);

        // Tenet movie information
        Posters tenet = new Posters();
        tenet.id = "tenet";
        tenet.image = R.drawable.poster_tenet;
        tenet.name = "Tenet";
        tenet.rating = 5f;
        tenet.createdBy = "Christopher Nolan";
        tenet.story = "A secret agent must manipulate the flow of time to prevent a global catastrophe.";
        posters.add(tenet);

        // Fight Club movie information
        Posters fightClub = new Posters();
        fightClub.id = "fight_club";
        fightClub.image = R.drawable.poster_fight_club;
        fightClub.name = "Fight Club";
        fightClub.rating = 5f;
        fightClub.createdBy = "David Fincher";
        fightClub.story = "An insomniac and a mysterious stranger create an underground fight club.";
        posters.add(fightClub);

        return posters;
    }


}
