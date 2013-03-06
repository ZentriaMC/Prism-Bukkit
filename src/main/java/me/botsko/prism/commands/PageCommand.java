package me.botsko.prism.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionMessage;
import me.botsko.prism.actionlibs.QueryResult;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.Flag;
import me.botsko.prism.commandlibs.SubHandler;
import me.botsko.prism.utils.TypeUtils;

public class PageCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Prism plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public PageCommand(Prism plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		if(call.getArgs().length != 2){
			call.getSender().sendMessage( Prism.messenger.playerError( "Please specify a page number. Like /prism page 2" ) );
			return;
		}
		
		if(!TypeUtils.isNumeric( call.getArg(1) )){
			call.getSender().sendMessage( Prism.messenger.playerError( "Page numbers need to actually be numbers. Like /prism page 2" ) );
			return;
		}
		
		int page = Integer.parseInt(call.getArg(1));

		if(page <= 0){
			call.getSender().sendMessage( Prism.messenger.playerError("Page must be greater than zero.") );
			return;
		}
		
		String keyName = "console";
		if( call.getSender() instanceof Player ){
//			Player player = call.getSender();
			keyName = call.getSender().getName();
		}
		
		// Is anything even cached?
		if(plugin.cachedQueries.containsKey( keyName )){
			QueryResult results = plugin.cachedQueries.get( keyName );
			results.setPage(page);
			
			// Refresh the query time and replace
			results.setQueryTime();
			plugin.cachedQueries.replace( keyName, results);
			
			// Results?
			if(!results.getActionResults().isEmpty()){
				call.getSender().sendMessage( Prism.messenger.playerHeaderMsg("Showing "+results.getTotal_results()+" results. Page "+page+" of "+results.getTotal_pages()) );
				List<Handler> paginated = results.getPaginatedActionResults();
				if(paginated != null){
					for(Handler a : paginated){
						ActionMessage am = new ActionMessage(a);
						if( results.getParameters().allowsNoRadius() || results.getParameters().hasFlag(Flag.EXTENDED) || plugin.getConfig().getBoolean("prism.messenger.always-show-extended") ){
							am.showExtended();
						}
						call.getSender().sendMessage( Prism.messenger.playerMsg( am.getMessage() ) );
					}
				} else {
					call.getSender().sendMessage( Prism.messenger.playerError( "Pagination can't find anything. Do you have the right page number?" ) );
				}
			} else {
				call.getSender().sendMessage( Prism.messenger.playerError( "Nothing found." + ChatColor.GRAY + " Either you're missing something, or we are." ) );
			}
		} else {
			call.getSender().sendMessage( Prism.messenger.playerError( "There's no lookup results to show. They may have expired." ) );
		}
	}
}