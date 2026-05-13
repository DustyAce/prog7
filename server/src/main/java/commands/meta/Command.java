package commands.meta;

import shared.requests.CommandRequest;

/**
 * Command interface
 */
public interface Command {
    /**
     * Execute the command
     *
     * @param cr command arguments
     */
    public void execute(CommandRequest cr);

    /**
     * Get a command's description
     * @return description
     */
    public String desc();
    public String getName();

}