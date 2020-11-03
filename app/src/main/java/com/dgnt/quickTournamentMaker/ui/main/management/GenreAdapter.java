import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dgnt.quickTournamentMaker.ui.main.management.GroupViewHolder;
import com.dgnt.quickTournamentMaker.ui.main.management.PersonViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

//TODO get rid of this soon
import java.util.List;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<GroupViewHolder, PersonViewHolder> {

    public GenreAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.list_item_genre, parent, false);
//        return new GenreViewHolder(view);
        return null;
    }

    @Override
    public PersonViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.list_item_artist, parent, false);
//        return new ArtistViewHolder(view);
        return null;
    }

    @Override
    public void onBindChildViewHolder(PersonViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
//        final Artist artist = ((Genre) group).getItems().get(childIndex);
//        holder.setArtistName(artist.getName());
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int flatPosition, ExpandableGroup group) {
//        holder.setGenreTitle(group);
    }
}